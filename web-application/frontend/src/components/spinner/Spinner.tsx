import React from 'react';
import styles from './spinner.module.css';

const Spinner: React.FC = () => (
    <div className={styles.circleBorder}>
        <div className={styles.circleCore}/>
    </div>
);

export default Spinner;