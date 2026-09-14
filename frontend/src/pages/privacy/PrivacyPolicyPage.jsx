import styles from './PrivacyPolicyPage.module.css'

function PrivacyPolicyPage() {
    return (
        <main className={styles.page}>
            <h1 className={styles.title}>Privacy policy</h1>
            <p className={styles.intro}>
                Say I Do is a wedding planning tool. This page explains what we store,
                why we store it, and for how long.
            </p>

            <section className={styles.section}>
                <h2 className={styles.heading}>What we store</h2>
                <ul className={styles.list}>
                    <li>Your email address, so you can sign in and we can contact you about your account.</li>
                    <li>Your password, stored as a hash. We never store or see the password itself.</li>
                    <li>The wedding details and tasks you enter yourself.</li>
                    <li>A login token in your browser, so you do not have to sign in on every page.</li>
                    <li>Your choice in the consent banner, so we do not ask again.</li>
                </ul>
            </section>

            <section className={styles.section}>
                <h2 className={styles.heading}>How long we keep it</h2>
                <p className={styles.body}>
                    Your account data is kept until you delete your account. The login
                    token is removed when you sign out. If you reject storage in the
                    consent banner, the token only lasts until you close the browser.
                </p>
            </section>

            <section className={styles.section}>
                <h2 className={styles.heading}>What we do not do</h2>
                <p className={styles.body}>
                    We do not use analytics or advertising, we do not track you across
                    other sites, and we do not sell or share your data with third parties.
                </p>
            </section>

            <section className={styles.section}>
                <h2 className={styles.heading}>Your rights</h2>
                <p className={styles.body}>
                    You can ask to see the data we hold about you, have it corrected, or
                    have it deleted. Contact us and we will handle it.
                </p>
            </section>
        </main>
    )
}

export default PrivacyPolicyPage
