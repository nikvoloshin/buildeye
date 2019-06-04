import React from 'react';
import {StyleableComponentProps} from "../../types/StyleableComponentProps";
import styles from "./key-value-list.module.css";

interface Entry {
    name: string
    value: string
    key?: any
}

interface Props extends StyleableComponentProps {
    header?: string
    entries: Array<Entry>
}

const KeyValueList: React.FC<Props> = ({className, header, entries}) => {
    const items = entries.map(({name, value, key}) => (
        <li key={key ? key : name} className={styles.item}>
            <span className={styles.name}>{name}</span>
            <span className={styles.value}>{value}</span>
        </li>
    ));

    return (
        <div className={className}>
            {header ? <h1 className={styles.header}>{header}</h1> : null}
            <ul className={styles.list}>{items}</ul>
        </div>
    );
};

export default KeyValueList;