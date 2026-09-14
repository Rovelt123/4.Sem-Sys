import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import CookieConsent from './components/CookieConsent.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
    <CookieConsent />
  </StrictMode>,
)
