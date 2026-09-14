import styles from './AuthStatusCard.module.css'

function AuthStatusCard({ status, message }) {
  return (
    <div className={styles.card}>
      <div className={`${styles.icon} ${styles[status]}`}>
        {status === 'loading' && (
          <span
            className={styles.spinner}
            aria-hidden="true"
          />
        )}

        {status === 'success' && (
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path
              d="M5 12.5 9.5 17 19 7.5"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        )}

        {status === 'error' && (
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path
              d="M12 8v5M12 16.5v.5"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
            />
          </svg>
        )}
      </div>

      {status === 'loading' && (
        <>
          <h1>Confirming email</h1>
          <p>{message}</p>
        </>
      )}

      {status === 'success' && (
        <>
          <h1>Email confirmed</h1>
          <p>{message}</p>

          <p className={styles.description}>
            Your email address has been verified and your account is ready
            to use.
          </p>
        </>
      )}

      {status === 'error' && (
        <>
          <h1>Unable to confirm email</h1>
          <p>{message}</p>

          <p className={styles.description}>
            The confirmation link may have expired or already been used.
          </p>
        </>
      )}
    </div>
  )
}

export default AuthStatusCard