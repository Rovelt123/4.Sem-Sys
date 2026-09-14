const CONSENT_KEY = 'consent'
const TOKEN_KEY = 'token'

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

export function saveToken(token, remember) {
    clearToken()
    pickStorage(remember).setItem(TOKEN_KEY, token)
}

// ________________________________________________________

export function getToken() {
    return localStorage.getItem(TOKEN_KEY) || sessionStorage.getItem(TOKEN_KEY)
}

// ________________________________________________________

export function clearToken() {
    localStorage.removeItem(TOKEN_KEY)
    sessionStorage.removeItem(TOKEN_KEY)
}
