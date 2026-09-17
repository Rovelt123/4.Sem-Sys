import styles from "./Navbar.module.css";

import Buttons from "./Buttons.jsx";

import { NavLink } from "react-router";

function Navbar({ hideButtons = false }) {
    return (
        <div className={styles.container}>
            <div className={styles.logoContainer}>
                <NavLink to="/" className={styles.logoLink}>
                    <p className={styles.logoText}>
                        Say <span className={styles.logoCursive}>I Do</span>
                    </p>
                </NavLink>
            </div>

            {!hideButtons && <Buttons />}
        </div>
    );
}

export default Navbar;