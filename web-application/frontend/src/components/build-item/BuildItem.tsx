import React from 'react';
import classNames from 'classnames';
import {pure} from 'recompose';
import {Link} from 'react-router-dom';
import {formatDuration} from '../../utils/utils';
import {StyleableComponentProps} from '../../types/StyleableComponentProps';
import {BuildPreview} from '../../types/BuildPreview'
import Date from './date/Date'
import SuccessFlag from './success-flag/SuccessFlag';
import styles from './build-item.module.css';

const BuildItem: React.FC<BuildPreview> = ({id, project, action, success, duration, date}) => (
    <Link className={styles.link} to={`/${id}`}>
        <div className={styles.item}>
            <SuccessFlag className={styles.successFlag} success={success}/>
            <Text className={styles.action}>{action}</Text>
            <Text className={styles.project}>{project}</Text>
            <Text className={styles.duration}>{formatDuration(duration)}</Text>
            <Date className={classNames(styles.text, styles.date)} date={date}/>
        </div>
    </Link>
);

const Text: React.FC<StyleableComponentProps> = ({className, children}) => (
    <div className={classNames(className, styles.text)}>{children}</div>
);

export default pure(BuildItem);