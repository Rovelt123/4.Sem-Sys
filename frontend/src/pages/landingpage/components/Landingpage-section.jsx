import { NavLink } from "react-router";
import styles from "./Landingpage-section.module.css";

import BigInfocard from "./BigInfocard";
import piecesLogo from "../../../assets/piecesLogo.png"
import familyLogo from "../../../assets/familyLogo.png"
import calenderLogo from "../../../assets/calenderLogo.png"
function LandingpageSection(){
    return(
        <div className={styles.container}>

            <div className={styles.hero}>

                <img className={styles.heroImage} src="/hero.jpg" alt="" />
                <div className={styles.heroWash}></div>

                <div className={styles.upperContainer}>

                    <span className={styles.monogram}>S</span>

                    <p className={styles.sloganText}>WEDDING PLANNING, MADE MANAGEABLE</p>

                    <h1 className={styles.titleText}>Weddings made easy to create and manage</h1>

                    <p className={styles.text}>
                        Do you need a simple way to make wedding planning more manageable?

                        Say I Do, helps you break your wedding into clear categories and manageable tasks. 
                        Create tasks, set deadlines, track your budget, and make changes whenever your plans change.

                        Whether you need to update the venue, add something to catering, or reorganize your tasks, everything stays in one place. 
                        This gives you a clear overview of what still needs to be done and helps make planning your wedding less stressful.
                    </p>

                    <NavLink to="/register" className={styles.signupButton}>Sign up, it's free</NavLink>

                    <p className={styles.smallText}>Already have an account?
                        <NavLink to="/login" className={styles.textLink}> Log in </NavLink>
                    </p>

                </div>

            </div>



            <div className={styles.midSectionContainer}>

                <p className={styles.sloganText}> HOW IT WORKS </p>
                <h2  className={styles.title2Text}> One place, the whole picture</h2>

            </div>



            <div className={styles.cardContainer}>
                
                <div className={styles.card}>
                    <img src={piecesLogo} width={30} height={30} alt="" />
                    <h3 className={styles.title3Text}>Break it into pieces</h3>
                    <p className={styles.text}>Your wedding becomes categories, and each category concrete tasks, so nothing feels overwhelming</p>
                </div>
                <div className={styles.card}>
                    <img src={calenderLogo} width={30} height={30} alt="" />
                    <h3 className={styles.title3Text}>Never miss a deadline</h3>
                    <p className={styles.text}>Every task gets a status and a due date, so you always know what's urgent</p>
                </div>
                <div className={styles.card}>
                    <img src={familyLogo} width={30} height={30} alt="" />
                    <h3 className={styles.title3Text}>Share the load</h3>
                    <p className={styles.text}>Give family and friends access to the tasks they've offered to help with</p>
                </div>

            </div>


            <BigInfocard/>


            <div className={styles.footerContainer}>

                <p className={styles.footerText}>Say I Do</p>
                <NavLink to="/privacy" className={styles.footerText}>Privacy policy</NavLink>

            </div>

        </div>
    );

}
export default LandingpageSection;
