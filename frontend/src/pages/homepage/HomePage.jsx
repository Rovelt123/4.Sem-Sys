import { useState } from 'react'
import { Link, useNavigate } from 'react-router'
import styles from './HomePage.module.css'
import TaskList from './components/TaskList.jsx'
import { getUser, clearSession } from '../../utils/storage'

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

function HomePage() {
  const user = getUser()
  const name = displayName(user)
  const wedding = PLACEHOLDER_WEDDING
  const [tasks, setTasks] = useState(PLACEHOLDER_TASKS)
  const navigate = useNavigate()
  

  const done = tasks.filter((task) => task.done).length
  const percent = Math.round((done / tasks.length) * 100)
  const days = daysUntil(wedding.date)

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

  return (
    <div className={styles.homePage}>
      <header className={styles.topBar}>
        <Link className={styles.wordmark} to="/">
          <img src="/logo.svg" alt="" />
          <span>Say <em>I Do</em></span>
        </Link>

        <div className={styles.account}>
          <span className={styles.accountName}>{name}</span>
          {!wedding && (<button className={styles.createWedding} onClick={handleCreateWedding}>Create wedding</button>)}
          <button className={styles.logout} onClick={handleLogout}>Log out</button>
        </div>
      </header>

      <main className={styles.content}>
        <p className={styles.eyebrow}>Your planning</p>
        <h1 className={styles.heading}>
          {name ? `Welcome back, ${name}` : 'Welcome back'}
        </h1>

        <section className={styles.summary}>
          <div className={styles.summaryMain}>
            <p className={styles.date}>
              {new Date(wedding.date).toLocaleDateString('en-GB', {
                day: 'numeric',
                month: 'long',
                year: 'numeric',
              })}
            </p>
            <h2 className={styles.weddingTitle}>{wedding.title}</h2>
            <p className={styles.venue}>{wedding.venue}</p>
          </div>

          <div className={styles.summaryStats}>
            <div className={styles.stat}>
              <span className={styles.statValue}>{days}</span>
              <span className={styles.statLabel}>days to go</span>
            </div>
            <div className={styles.stat}>
              <span className={styles.statValue}>{wedding.guests}</span>
              <span className={styles.statLabel}>guests</span>
            </div>
            <div className={styles.stat}>
              <span className={styles.statValue}>{percent}%</span>
              <span className={styles.statLabel}>done</span>
            </div>
          </div>

          <div className={styles.progress}>
            <div className={styles.bar} style={{ width: percent + '%' }}></div>
          </div>
        </section>

        <div className={styles.sectionHead}>
          <h2 className={styles.sectionTitle}>Tasks</h2>
          <span className={styles.sectionCount}>{done} of {tasks.length} done</span>
        </div>

        <TaskList tasks={tasks} onToggle={toggleTask} />
      </main>

      <footer className={styles.footer}>
        <Link className={styles.footerLink} to="/privacy">Privacy policy</Link>
      </footer>
    </div>
  )
}

export default HomePage
