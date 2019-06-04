import React from 'react';
import {pure} from "recompose";
import classNames from 'classnames';
import {NavLink} from "react-router-dom";
import {StyleableComponentProps} from "../../types/StyleableComponentProps";
import styles from './build-sidebar.module.css';

interface Props extends StyleableComponentProps {
    id: number
}

const BuildSidebar: React.FC<Props> = ({className, id}) => {
    return (
        <ul className={classNames(className, styles.nav)}>
            <NavItem id={id} path="">Summary</NavItem>
            <NavItem id={id} path="execution">Tasks execution</NavItem>
        </ul>
    );
};

const NavItem: React.FC<{id: number, path: string}> = ({path, children, id}) => (
    <NavLink
        exact
        className={styles.item}
        activeClassName={styles.itemActive}
        to={`/${id}/${path}`}
    >
        {children}
    </NavLink>
);

export default pure(BuildSidebar);