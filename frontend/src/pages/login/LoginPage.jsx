import { useState } from 'react'
import './LoginPage.css'

const API_BASE = import.meta.env.VITE_API_URL ?? 'http://localhost:9292/api'

// ________________________________________________________

function parseErrorMessage(text) {
  try {
    return JSON.parse(text).message || text
  } catch {
    return text
  }
}

// ________________________________________________________

function LoginPage({ onLoginSuccess }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  // ________________________________________________________

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      const response = await fetch(`${API_BASE}/users/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      })

      if (!response.ok) {
        const text = await response.text()
        setError(parseErrorMessage(text) || 'Login failed')
        return
      }

      const result = await response.json()

      localStorage.setItem('token', result.data.token)
      localStorage.setItem('user', JSON.stringify(result.data.data))

      if (onLoginSuccess) {
        onLoginSuccess(result.data.data)
      }
    } catch {
      setError('Could not connect to the server')
    } finally {
      setLoading(false)
    }
  }

  // ________________________________________________________

  return (
    <div className="login-page">
      <div className="login-decor" aria-hidden="true"></div>

      <a className="login-wordmark" href="/">
        <img src="/logo.svg" alt="" />
        <span>Say <em>I Do</em></span>
      </a>

      <form className="login-card" onSubmit={handleSubmit}>
        <h1>Log in</h1>
        <p className="login-subtitle">Welcome back</p>

        {error && <p className="login-error">{error}</p>}

        <label htmlFor="email">Email</label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <label htmlFor="password">Password</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button type="submit" disabled={loading}>
          {loading ? 'Logging in...' : 'Log in'}
        </button>
      </form>

      <p className="login-secondary">
        No account yet? <a href="/register">Register</a>
      </p>
    </div>
  )
}

export default LoginPage
