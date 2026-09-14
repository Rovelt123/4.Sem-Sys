import styles from "./Navbar.module.css";
import { NavLink } from "react-router";
function Navbar(){
    return(
        <div className={styles.container}>
            <div className={styles.logoContainer}>
                <NavLink to="/" className={styles.logoLink}>
                    <p className={styles.logoText}>
                        Say <span className={styles.logoCursive}>I Do</span>
                    </p>
                </NavLink>
            </div>
            
            <div className={styles.loginRegisterContainer}>
                
                <NavLink to="/login" className={styles.loginButton}>
                    Login
                </NavLink>
                    
                <NavLink to="/register" className={styles.signupButton}>
                    Sign up
                </NavLink>
            </div>
        </div>
    );

}

export default Navbar;
