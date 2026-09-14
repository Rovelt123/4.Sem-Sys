const CONSENT_KEY = 'consent'
const TOKEN_KEY = 'token'
const USER_KEY = 'user'

// ________________________________________________________

export function getConsent() {
    return localStorage.getItem(CONSENT_KEY)
}

// ________________________________________________________

export function setConsent(choice) {
    localStorage.setItem(CONSENT_KEY, choice)
}

// ________________________________________________________

function pickStorage(remember) {
    if (getConsent() === 'rejected') {
        return sessionStorage
    }

    return remember ? localStorage : sessionStorage
}

// ________________________________________________________

export function saveSession(token, user, remember) {
    clearSession()

    const store = pickStorage(remember)
    store.setItem(TOKEN_KEY, token)
    store.setItem(USER_KEY, JSON.stringify(user))
}

// ________________________________________________________

export function getToken() {
    return localStorage.getItem(TOKEN_KEY) || sessionStorage.getItem(TOKEN_KEY)
}

// ________________________________________________________

export function getUser() {
    const raw = localStorage.getItem(USER_KEY) || sessionStorage.getItem(USER_KEY)

    try {
        return JSON.parse(raw)
    } catch {
        return null
    }
}

// ________________________________________________________

export function clearSession() {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
    sessionStorage.removeItem(TOKEN_KEY)
    sessionStorage.removeItem(USER_KEY)
}
