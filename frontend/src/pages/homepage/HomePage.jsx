import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router'
import styles from './HomePage.module.css'
import TaskList from './components/TaskList.jsx'
import CategoryColumn from './components/CategoryColumn.jsx'
import {getToken, getUser, clearSession } from '../../utils/storage'
const API_BASE = import.meta.env.VITE_API_URL ?? 'http://localhost:9292/api'

const PLACEHOLDER_WEDDING = {
  title: 'Our wedding',
  date: '2027-06-12',
  venue: 'Kokkedal Slot',
  guests: 84,
}

const PLACEHOLDER_TASKS = [
  { id: 1, title: 'Book the venue', category: 'Venue', due: '2026-10-01', priority: 'High', done: true },
  { id: 2, title: 'Send save the date', category: 'Guests', due: '2026-11-15', priority: 'High', done: true },
  { id: 3, title: 'Find a photographer', category: 'Vendors', due: '2027-02-01', priority: 'High', done: false },
  { id: 4, title: 'Choose the menu', category: 'Catering', due: '2027-01-20', priority: 'Medium', done: false },
  { id: 5, title: 'Order the cake', category: 'Catering', due: '2027-03-10', priority: 'Medium', done: false },
  { id: 6, title: 'Plan the seating', category: 'Guests', due: '2027-05-01', priority: 'Low', done: false },
]



// ________________________________________________________

function displayName(user) {
  if (!user) {
    return ''
  }

  if (user.first_name) {
    return user.first_name
  }

  if (user.name) {
    return user.name
  }

  if (user.email) {
    return user.email.split('@')[0]
  }

  return ''
}

// ________________________________________________________

function daysUntil(date) {
  const diff = new Date(date) - new Date()
  return Math.ceil(diff / (1000 * 60 * 60 * 24))
}

// ________________________________________________________

function parseErrorMessage(text) {
  try {
    const parsed = JSON.parse(text)

    if (typeof parsed === 'string') {
      return parsed
    }

    return parsed.message || text
  } catch {
    return text
  }
}

// ________________________________________________________

function HomePage() {
  const user = getUser()
  const name = displayName(user)

  const [wedding, setWedding] = useState(null)
  const [loadingWedding, setLoadingWedding] = useState(true)

  const [showDeleteWarning, setShowDeleteWarning] = useState(false)

  const [tasks, setTasks] = useState(PLACEHOLDER_TASKS)
  const navigate = useNavigate()

  const [showEditWedding, setShowEditWedding] = useState(false)
  //
  const [editForm, setEditForm] = useState({
    title: '',
    date: '',
    location: '',
    budget: '',
    description: '',
  })

 
  const [editError, setEditError] = useState('')
  const [editingWedding, setEditingWedding] = useState(false)

  const [categories, setCategories] = useState([])

  const [editFormCategory, setEditFormCategory] = useState({
    title: '',
  })
  const [showEditCategory, setShowEditCategory] = useState(false)
  const [editingCategory, setEditingCategory] = useState(null)
  const [categoryToDelete, setCategoryToDelete] = useState(null)
  const [showDeleteCategoryWarning, setShowDeleteCategoryWarning] = useState(false)
  
  
  const [showCreateCategory, setShowCreateCategory] = useState(false)
  const [createCategoryForm, setCreateCategoryForm] = useState({
    title: '',
  })
  const [error, setError] = useState('')

  const done = tasks.filter((task) => task.done).length
  const percent = Math.round((done / tasks.length) * 100)
  const days = wedding ? daysUntil(wedding.date) : 0
    // ________________________________________________________

  useEffect(() => {

    const loadWedding = async () => {

      try {
        const response = await fetch(`${API_BASE}/weddings`, {
          headers: {
            Authorization: `Bearer ${getToken()}`,
          },
        })

        
        if (response.status === 204) {
          setWedding(null)
          return
        }

        if (!response.ok) {
          throw new Error('Could not load wedding')
        }

        const result = await response.json()

        const weddings = result.data.data

        if (weddings.length > 0) {
          setWedding(weddings[0])
        } else {
          setWedding(null)
        }

      } catch (error) {
        console.error('Could not load wedding:', error)
        setWedding(null)

      } finally {
        setLoadingWedding(false)
      }
    }

    loadWedding()

  }, [])

  useEffect(() => {
  if (!wedding) {
    return
  }

  const loadCategories = async () => {
    try {
      const response = await fetch(
        `${API_BASE}/weddings/${wedding.id}/categories`,
        {
          headers: {
            Authorization: `Bearer ${getToken()}`,
          },
        }
      )

      if (!response.ok) {
        throw new Error('Could not load categories')
      }

      const result = await response.json()
      const categories = result.data.data

      console.log('Category response:', result)
      console.log('Categories:', categories)

      setCategories(categories)

    } catch (error) {
      console.error('Could not load categories:', error)
      setCategories([])
    }
  }

  loadCategories()
}, [wedding])




  // ________________________________________________________
  const toggleTask = (id) => {
    setTasks(tasks.map((task) =>
      task.id === id ? { ...task, done: !task.done } : task
    ))
  }

  // ________________________________________________________

  const handleLogout = () => {
    clearSession()
    navigate('/login')
  }

  // ________________________________________________________

  const handleCreateWedding = () => {
    navigate('/create')
  }

  // ________________________________________________________

    const handleDeleteWedding = async () => {
      try {
        const response = await fetch(
          `${API_BASE}/weddings/${wedding.id}`,
          {
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${getToken()}`,
            },
          }
        )

        if (!response.ok) {
          throw new Error('Could not delete wedding')
        }

        setWedding(null)
        setShowDeleteWarning(false)

      } catch (error) {
        console.error('Could not delete wedding:', error)
      }
    }
  // ________________________________________________________

    const handleEditWedding = async (e) => {
      e.preventDefault()

      setEditError('')
      setEditingWedding(true)

      const body = {
        title: editForm.title,
        date: editForm.date,
        location: editForm.location,
        description: editForm.description,
      }

      if (editForm.budget !== '') {
        body.budget = editForm.budget
      }

      try {
        const response = await fetch(
          `${API_BASE}/weddings/${wedding.id}`,
          {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
              Authorization: `Bearer ${getToken()}`,
            },
            body: JSON.stringify(body),
          }
        )

        if (!response.ok) {
          const text = await response.text()
          setEditError(
            parseErrorMessage(text) || 'Could not update wedding'
          )
          return
        }

        setWedding({
          ...wedding,
          ...body,
        })

        setShowEditWedding(false)

      } catch {
        setEditError('Could not connect to the server')
      } finally {
        setEditingWedding(false)
      }
    }

  // ________________________________________________________
    const handleOpenEdit = () => {
    setEditForm({
      title: wedding.title ?? '',
      date: wedding.date ?? '',
      location: wedding.location ?? '',
      budget: wedding.budget != null? String(wedding.budget): '',
      description: wedding.description ?? '',
    })

    setEditError('')
    setShowEditWedding(true)
  }

  // ________________________________________________________

    const handleEditChange = (e) => {
      setEditForm({...editForm, [e.target.name]: e.target.value,})
    }

  // ________________________________________________________

  const handleEditChangeCategory = (e) => {
      setEditFormCategory({...editFormCategory, [e.target.name]: e.target.value,})
    }

  // ________________________________________________________

   const handleOpenEditCategory = (category) => {
      setEditingCategory(category)
      setEditFormCategory({
        title: category.title ?? '',
      })
      setEditError('')
      setShowEditCategory(true)
    }

// ________________________________________________________

    const handleOpenDeleteCategory = (category) => {
      setCategoryToDelete(category)
      setShowDeleteCategoryWarning(true)
    }

// ________________________________________________________

const handleEditCategory = async (e) => {
      e.preventDefault()

      setEditError('')
      

      const body = {
        title: editFormCategory.title,
      }

      try {
        const response = await fetch(
          `${API_BASE}/categories/${editingCategory.id}`,
          {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
              Authorization: `Bearer ${getToken()}`,
            },
            body: JSON.stringify(body),
          }
        )

        if (!response.ok) {
          const text = await response.text()
          setEditError(
            parseErrorMessage(text) || 'Could not update category'
          )
          return
        }

        setCategories(
          categories.map((category) =>
            category.id === editingCategory.id
              ? { ...category, ...body }
              : category
          )
        )

        setShowEditCategory(false)

      } catch {
        setEditError('Could not connect to the server')
      } finally {
        setEditingCategory(false)
      }
    }

  // ________________________________________________________

  const handleDeleteCategory = async () => {
    try {
      const response = await fetch(
        `${API_BASE}/categories/${categoryToDelete.id}`,
        {
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${getToken()}`,
          },
        }
      )

      if (!response.ok) {
        const text = await response.text()
        console.log('DELETE error response:', text)
        throw new Error('Could not delete category')
      }

      setCategories(
        categories.filter(
          category => category.id !== categoryToDelete.id
        )
      )

      setCategoryToDelete(null)
      setShowDeleteCategoryWarning(false)

    } catch (error) {
      console.error('Could not delete category:', error)
    }
  }
  

  // ________________________________________________________

  const handleCreateCategory = async (e) => {
    e.preventDefault()
    setError('')


    const body = {
      title: createCategoryForm.title,
      
    }
    try {
      const response = await fetch(
        `${API_BASE}/weddings/${wedding.id}/categories`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${getToken()}`,
          },
          body: JSON.stringify(body),
        }
      )

      if (!response.ok) {
        const text = await response.text()
        setEditError(parseErrorMessage(text) || 'Could not create the wedding')
        return
      }

      const result = await response.json()

      const createdCategory = result.data.data

      setCategories([
        ...categories,
        createdCategory
      ])

      setCreateCategoryForm({
        title: '',
      })

      setShowCreateCategory(false)

    } catch {
      setEditError('Could not connect to the server')
    }
  }

  // ________________________________________________________

  const handleCreateChangeCategory = (e) => {
    setCreateCategoryForm({ ...createCategoryForm, [e.target.name]: e.target.value })
  }

 

  return (
    <div className={styles.homePage}>
      <header className={styles.topBar}>
        <Link className={styles.wordmark} to="/">
          <img src="/logo.svg" alt="" />
          <span>Say <em>I Do</em></span>
        </Link>

        <div className={styles.account}>
          <span className={styles.accountName}>{name}</span>
          {!loadingWedding && !wedding && (<button className={styles.createWedding} onClick={handleCreateWedding}>Create wedding</button>)}
          {!loadingWedding && wedding && (<button className={styles.deleteWedding} onClick={() => setShowDeleteWarning(true)}>Delete wedding</button>)}
          <button className={styles.logout} onClick={handleLogout}>Log out</button>
        </div>
      </header>

      <main className={styles.content}>
        <p className={styles.eyebrow}>Your planning</p>
        <h1 className={styles.heading}>
          {name ? `Welcome back, ${name}` : 'Welcome back'}
        </h1>

        {loadingWedding && (
          <p>Loading wedding...</p>
        )}

        {!loadingWedding && !wedding && (
          <section className={styles.summary}>
            <h2>You haven't created a wedding yet</h2>
            <p>Use the Create wedding button to get started.</p>
          </section>
        )}

        {!loadingWedding && wedding && (
          <>
            <section className={styles.summary}>
              <button className={styles.editWedding} onClick={handleOpenEdit}> Edit </button>
              <button className={styles.createCategoryButton} onClick={() => setShowCreateCategory(true)}> Category + </button>
              <div className={styles.summaryMain}>
                <p className={styles.date}>
                  {new Date(wedding.date).toLocaleDateString('en-GB', {
                    day: 'numeric',
                    month: 'long',
                    year: 'numeric',
                  })}
                </p>

                <h2 className={styles.weddingTitle}> {wedding.title} </h2>

                <p className={styles.venue}> {wedding.location} </p>
              </div>

              <div className={styles.summaryStats}>

                <div className={styles.stat}>
                  <span className={styles.statValue}> {days} </span>
                  <span className={styles.statLabel}> days to go </span>
                </div>

                <div className={styles.stat}>
                  <span className={styles.statValue}> {percent}% </span>
                  <span className={styles.statLabel}> done </span>
                </div>
              </div>

              <div className={styles.progress}>
                <div className={styles.bar} style={{ width: percent + '%' }} />
              </div>
            </section>

            
          </>
        )}
      <div className={styles.categoryBoard}>
        {categories.map((category) => (
          <CategoryColumn
            key={category.id}
            category={category} onEdit={handleOpenEditCategory} onDelete={handleOpenDeleteCategory}
          />
        ))}
      </div>
      </main>

      <footer className={styles.footer}>
        <Link className={styles.footerLink} to="/privacy"> Privacy policy </Link>
      </footer>

      {showDeleteWarning && (
        <div className={styles.modalOverlay}>
          <div className={styles.deleteModal}>
            <h2>Delete wedding?</h2>

            <p> Are you sure you want to delete this wedding?</p>

            <div className={styles.modalActions}>
              <button className={styles.deleteWedding} onClick={() => setShowDeleteWarning(false)}>Cancel</button>
              <button className={styles.deleteWedding} onClick={handleDeleteWedding}>Delete</button>
            </div>
          </div>
        </div>
      )}

        {showEditWedding && ( <div className={styles.modalOverlay}>

            <form className={styles.editModal} onSubmit={handleEditWedding}>
              <h2>Edit wedding</h2>

              {editError && (<p className={styles.editError}> {editError} </p> )}

              <label htmlFor="edit-title">
                Title
              </label>

              <input id="edit-title" name="title" type="text" value={editForm.title} onChange={handleEditChange} required />

              <label htmlFor="edit-date">
                Date
              </label>

              <input id="edit-date" name="date" type="date" value={editForm.date} onChange={handleEditChange} required />

              <label htmlFor="edit-location">
                Location
              </label>

              <input id="edit-location" name="location" type="text" value={editForm.location} onChange={handleEditChange} />

              <label htmlFor="edit-budget"> Budget </label>

              <input id="edit-budget" name="budget" type="number" min="0" step="1" value={editForm.budget} onChange={handleEditChange} />

              <label htmlFor="edit-description">
                Description
              </label>

              <textarea id="edit-description" name="description" rows="4" value={editForm.description} onChange={handleEditChange} required />

              <div className={styles.modalActions}>

                <button className={styles.createWedding} type="button" onClick={() => setShowEditWedding(false)}>
                  Cancel
                </button>

                <button className={styles.createWedding} type="submit" disabled={editingWedding}>
                  {editingWedding ? 'Saving...' : 'Save changes'}
                </button>

              </div>
            </form>

          </div>
        )}

        {showDeleteCategoryWarning && categoryToDelete && (
        <div className={styles.modalOverlay}>
          <div className={styles.deleteModal}>
            <h2>Delete category?</h2>

            <p> Are you sure you want to delete this Category?</p>

            <div className={styles.modalActions}>
              <button className={styles.deleteWedding} onClick={() => {setShowDeleteCategoryWarning(false), setCategoryToDelete(null)}}>Cancel</button>
              <button className={styles.deleteWedding} onClick={handleDeleteCategory}>Delete</button>
            </div>
          </div>
        </div>
      )}

        {showEditCategory && (
          <div className={styles.modalOverlay}>

            <form className={styles.editCategoryModal} onSubmit={handleEditCategory}>
              <h2>Edit category</h2>
              {editError && (
                <p className={styles.editError}> {editError} </p>
              )}

              <label htmlFor="category-title"> Category name </label>

              <input
                id="category-title"
                name="title"
                type="text"
                value={editFormCategory.title}
                onChange={handleEditChangeCategory}
                required
              />

              <div className={styles.modalActions}>
                <button className={styles.deleteWedding} type="button" onClick={() => {setShowEditCategory(false), setEditingCategory(null)}} >
                  Cancel
                </button>

                <button className={styles.createWedding} type="submit">
                  Save changes
                </button>
              </div>
            </form>

          </div>
        )}

        {showCreateCategory && (
          <div className={styles.modalOverlay}>
            <form className={styles.editCategoryModal} onSubmit={handleCreateCategory}>
              <h2>Create category</h2>
              {editError && (
                <p className={styles.editError}> {editError} </p>
              )}
              <label htmlFor="category-title"> Title </label>
              <input
                id="category-title"
                name="title"
                type="text"
                value={createCategoryForm.title}
                onChange={handleCreateChangeCategory}
                required
              />

              <div className={styles.modalActions}>
                <button className={styles.deleteWedding} type="button" onClick={() => {setShowCreateCategory(false)}} >
                  Cancel
                </button>

                <button className={styles.createWedding} type="submit">
                  Create category
                </button>
              </div>
            </form>
          </div>
        )}

    </div>
  )
}

export default HomePage
