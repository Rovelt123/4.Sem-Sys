import { NavLink } from "react-router";
import styles from "./BigInfocard.module.css";
import { getToken } from "../../../utils/storage";

function BigInfocard(){
    const target = getToken() ? "/homepage" : "/register";

    return(
        <div className={styles.container}>
            <h1 className={styles.titleText}>
                Ready to get started?
            </h1>
            <NavLink to={target} className={styles.createButton}>
                Create your wedding
            </NavLink>
        </div>

    );
}

export default BigInfocard;
