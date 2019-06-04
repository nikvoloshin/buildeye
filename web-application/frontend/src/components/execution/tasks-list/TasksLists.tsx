import React, {ReactElement} from 'react';
import classNames from "classnames";
import {formatDuration} from "../../../utils/utils";
import {StyleableComponentProps} from "../../../types/StyleableComponentProps";
import {Change, OutOfDateReason, Reason, TaskInfo} from "../../../types/BuildInfo";
import Dropdown from "../../dropdown/Dropdown";
import styles from './tasks-list.module.css';

interface Props extends StyleableComponentProps {
    tasks: Array<TaskInfo>
}

const TasksList: React.FC<Props> = ({className, tasks}) => {
    const items = tasks.map(task => <li key={task.path}><Task task={task}/></li>);
    return (
        <div>
            <ListHeader/>
            <ul className={classNames(className, styles.list)}>
                {items}
            </ul>
        </div>
    )
};

const ListHeader: React.FC = () => {
    return (
        <div className={classNames(styles.listHeader)}>
            <div className={styles.path}>Path</div>
            <div className={styles.startedAfter}>Started after</div>
            <div className={styles.duration}>Duration</div>
        </div>
    );
};

const Task: React.FC<{ task: TaskInfo }> = ({task}) => {
    const trigger = <TaskHeader task={task}/>;
    const content = <TaskContent task={task}/>;
    return (
        <Dropdown
            activeClassName={styles.dropdownActive}
            trigger={trigger}
            content={content}
        />
    );
};

const TaskHeader: React.FC<{ task: TaskInfo }> = ({task}) => {
    return (
        <div className={styles.header}>
            <div className={styles.path}>{task.path}</div>
            <div className={styles.startedAfter}>{formatDuration(task.startedOffset)}</div>
            <div className={styles.duration}>{formatDuration(task.duration)}</div>
        </div>
    )
};

const TaskContent: React.FC<{ task: TaskInfo }> = ({task}) => {
    const items = [
        {name: "Executed", value: task.taskStateInfo.executed},
        {name: "Did work", value: task.taskStateInfo.didWork},
        {name: "Skipped", value: task.taskStateInfo.skipped},
        {name: "Up-to-date", value: task.taskStateInfo.upToDate},
        {name: "No source", value: task.taskStateInfo.noSource}
    ].map(({name, value}) => <StateItem name={name} value={value ? "Yes" : "No"}/>);

    return (
        <div className={styles.content}>
            <div>{items}</div>
            <div className={styles.message}>
                {task.taskStateInfo.skipMessage ?
                    `Task skipped with a message: ${task.taskStateInfo.skipMessage}` :
                    null
                }
            </div>
            <div className={styles.message}>
                {!task.taskStateInfo.upToDate ?
                    <div>{renderOutOfDate(task.taskStateInfo.outOfDateReason as OutOfDateReason)}</div> :
                    null
                }
            </div>
        </div>
    );
};

const StateItem: React.FC<{ name: string, value: string }> = ({name, value}) => (
    <div className={styles.stateItem}>
        <span className={styles.stateItemName}>{name}</span>
        <span>{value}</span>
    </div>
);

const renderOutOfDate = (outOfDateReason: OutOfDateReason): ReactElement | null => {
    switch (outOfDateReason.reason) {
        case Reason.UNKNOWN:
            return null;
        case Reason.INPUTS_CHANGE: {
            return (
                <>
                    <div>Task is not up-to-date because of the following changes</div>
                    <ul>
                        {(outOfDateReason.changes as Array<Change>).map(change =>
                            <div>{`${change.inputType} ${change.name} (${change.changeType})`}</div>
                        )}
                    </ul>
                </>
            )
        }
    }
};

export default TasksList;