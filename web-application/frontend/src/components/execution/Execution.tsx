import React from 'react';
import {StyleableComponentProps} from "../../types/StyleableComponentProps";
import {ExecutionInfo} from "../../types/BuildInfo";
import TasksList from "./tasks-list/TasksLists";
import styles from './execution.module.css';

interface Props extends StyleableComponentProps {
    executionInfo: ExecutionInfo
}

const Execution: React.FC<Props> = ({className, executionInfo}) => {
    return (
        <div className={className}>
            <h1 className={styles.header}>Execution</h1>
            <TasksList tasks={executionInfo.taskInfos} />
        </div>
    );
};

export default Execution;