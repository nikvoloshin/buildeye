import React, {useEffect, useMemo, useState} from 'react';
import {fetchAllBuilds} from "../../api/Api";
import {BuildPreview} from "../../types/BuildPreview";
import BuildItem from "../build-item/BuildItem";
import Spinner from "../spinner/Spinner";
import styles from './builds-list.module.css';
import itemStyles from '../build-item/build-item.module.css';
import classNames from "classnames";

const BuildsList: React.FC = () => {
    const [builds, setBuilds] = useState([] as Array<BuildPreview>);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        fetchAllBuilds((array) => {
            setBuilds(array.map(build => ({
                id: build.id,
                success: !(build.buildInfo.buildResultInfo.failure),
                project: build.buildInfo.project,
                action: build.buildInfo.buildResultInfo.action,
                duration: build.buildInfo.executionInfo.duration,
                date: new Date(build.buildInfo.executionInfo.executionStartedDate)
            })));
            setIsLoading(() => false);
        })
    }, []);

    const items = useMemo(() =>
            builds.map(build => (
                <li key={build.id}><BuildItem {...build} /></li>
            )),
        [builds]
    );

    return (isLoading ?
            <Spinner/> :
            <div>
                <ListHeader/>
                <ul className={styles.list}>{items}</ul>
            </div>
    );
};

const ListHeader: React.FC = () => {
    return (
        <div className={classNames(itemStyles.item, styles.header)}>
            <div className={styles.action}>Action</div>
            <div className={styles.project}>Project</div>
            <div className={styles.duration}>Duration</div>
            <div className={styles.date}>Started at</div>
        </div>
    );
};

export default BuildsList;