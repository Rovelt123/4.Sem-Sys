import { useState } from 'react'
import './RegisterPage.css'

const API_BASE = import.meta.env.VITE_API_URL ?? 'http://localhost:9292/api'

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

function RegisterPage({ onRegisterSuccess }) {
  const [form, setForm] = useState({
    first_name: '',
    last_name: '',
    email: '',
    password: '',
    repeat_password: '',
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  // ________________________________________________________

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  // ________________________________________________________

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')

    if (form.password !== form.repeat_password) {
      setError('The passwords do not match')
      return
    }

    setLoading(true)

    try {
      const response = await fetch(`${API_BASE}/users/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form),
      })

      if (!response.ok) {
        const text = await response.text()
        setError(parseErrorMessage(text) || 'Registration failed')
        return
      }

      const result = await response.json()

      localStorage.setItem('token', result.data.token)
      localStorage.setItem('user', JSON.stringify(result.data.data))

      if (onRegisterSuccess) {
        onRegisterSuccess(result.data.data)
      }
    } catch {
      setError('Could not connect to the server')
    } finally {
      setLoading(false)
    }
  }

  // ________________________________________________________

  return (
    <div className="register-page">
      <div className="register-decor" aria-hidden="true"></div>

      <a className="register-wordmark" href="/">
        <img src="/logo.svg" alt="" />
        <span>Say <em>I Do</em></span>
      </a>

      <form className="register-card" onSubmit={handleSubmit}>
        <h1>Create account</h1>
        <p className="register-subtitle">Start planning your big day</p>

        {error && <p className="register-error">{error}</p>}

        <div className="register-row">
          <div>
            <label htmlFor="first_name">First name</label>
            <input
              id="first_name"
              name="first_name"
              type="text"
              value={form.first_name}
              onChange={handleChange}
              required
            />
          </div>

          <div>
            <label htmlFor="last_name">Last name</label>
            <input
              id="last_name"
              name="last_name"
              type="text"
              value={form.last_name}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        <label htmlFor="email">Email</label>
        <input
          id="email"
          name="email"
          type="email"
          value={form.email}
          onChange={handleChange}
          required
        />

        <label htmlFor="password">Password</label>
        <input
          id="password"
          name="password"
          type="password"
          value={form.password}
          onChange={handleChange}
          required
        />

        <label htmlFor="repeat_password">Repeat password</label>
        <input
          id="repeat_password"
          name="repeat_password"
          type="password"
          value={form.repeat_password}
          onChange={handleChange}
          required
        />

        <button type="submit" disabled={loading}>
          {loading ? 'Creating account...' : 'Create account'}
        </button>
      </form>

      <p className="register-secondary">
        Already have an account? <a href="/login">Log in</a>
      </p>
    </div>
  )
}

export default RegisterPage
