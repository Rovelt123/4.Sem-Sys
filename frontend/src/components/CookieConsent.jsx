import { useState } from 'react'
import { Link } from 'react-router'
import { getConsent, setConsent } from '../utils/storage'
import styles from './CookieConsent.module.css'

function CookieConsent() {
    const [visible, setVisible] = useState(getConsent() === null)

    // ________________________________________________________

    function choose(choice) {
        setConsent(choice)
        setVisible(false)
    }

    // ________________________________________________________

    if (!visible) {
        return null
    }

    return (
        <div className={styles.banner}>
            <div className={styles.text}>
                <h2 className={styles.title}>Your data, your choice</h2>
                <p className={styles.body}>
                    We store a login token so you do not have to sign in again, and we
                    remember this choice. Rejecting keeps you signed in for this browser
                    session only. Read more in our{' '}
                    <Link className={styles.link} to="/privacy">privacy policy</Link>.
                </p>
            </div>

            <div className={styles.actions}>
                <button className={styles.ghost} onClick={() => choose('rejected')}>
                    Reject
                </button>
                <button className={styles.ghost} onClick={() => choose('necessary')}>
                    Necessary only
                </button>
                <button className={styles.primary} onClick={() => choose('all')}>
                    Accept all
                </button>
            </div>
        </div>
    )
}

export default CookieConsent
