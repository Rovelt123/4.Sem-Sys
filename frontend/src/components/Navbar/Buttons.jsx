import styles from "./Navbar.module.css";
import { NavLink } from "react-router";

function Buttons(){
    return(
        <div className={styles.loginRegisterContainer}>
            <NavLink to="/login" className={styles.loginButton}>
                Login
            </NavLink>
                
            <NavLink to="/register" className={styles.signupButton}>
                Sign up
            </NavLink>
        </div>
    );

}

export default Buttons;


