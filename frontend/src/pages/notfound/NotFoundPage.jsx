import { Link } from 'react-router'
import styles from './NotFoundPage.module.css'

function NotFoundPage() {
  return (
    <main className={styles.page}>
      <p className={styles.code}>404</p>
      <h1 className={styles.title}>This page does not exist</h1>
      <p className={styles.body}>
        The address may be misspelled, or the page may have been moved.
      </p>
      <Link className={styles.link} to="/">Back to the front page</Link>
    </main>
  )
}

export default NotFoundPage
