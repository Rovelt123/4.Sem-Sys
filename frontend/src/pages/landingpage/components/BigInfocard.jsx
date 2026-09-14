import { NavLink } from "react-router";
import styles from "./BigInfocard.module.css";
function BigInfocard(){
    return(
        <div className={styles.container}>
            <h1 className={styles.titleText}>
                Ready to get started?
            </h1>
            <NavLink to="/" className={styles.createButton}>
                Create your wedding
            </NavLink>
        </div>

    );
}

export default BigInfocard;