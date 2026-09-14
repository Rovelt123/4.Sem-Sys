import styles from "./Landingpage.module.css";
import Navbar from "./components/Navbar";
import LandingpageSection from "./components/Landingpage-section";
function Landingpage(){

    return(
        <div className={styles.container}>
            <div className={styles.navbarContainer}>
                <Navbar/>
            </div>

            <div className={styles.landingSectionContainer}>
                <LandingpageSection />
            </div>
        </div>
    );
}

export default Landingpage;