import React from 'react';
import {pure} from "recompose";
import classNames from 'classnames';
import {StyleableComponentProps} from "../../types/StyleableComponentProps";
import styles from './header.module.css';

const Header: React.FC<StyleableComponentProps> = ({className}) => (
    <header className={classNames(className, styles.header)}>
        <a href="/" className={styles.link}>
            <div className={styles.logo}>BuildEye</div>
        </a>
    </header>
);

export default pure(Header);