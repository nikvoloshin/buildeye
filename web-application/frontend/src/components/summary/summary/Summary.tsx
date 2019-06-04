import React from 'react';
import {pure} from "recompose";
import {formatDate, formatDuration, formatNumeral} from "../../../utils/utils";
import {StyleableComponentProps} from "../../../types/StyleableComponentProps";
import {BuildInfo, TaskInfo} from "../../../types/BuildInfo";
import KeyValueList from "../../key-value-list/KeyValueList";

interface Props extends StyleableComponentProps {
    buildInfo: BuildInfo
}

const Summary: React.FC<Props> = ({className, buildInfo}) => {
    const items = [
        { name: "Project", value: buildInfo.project },
        { name: "Started on", value: formatDate(new Date(buildInfo.executionInfo.executionStartedDate)) },
        { name: "Build action", value: buildInfo.buildResultInfo.action },
        { name: "Duration", value: formatDuration(buildInfo.executionInfo.duration) },
        { name: "Execution", value: formatTasksSummary(buildInfo.executionInfo.taskInfos) }
    ];

    return <KeyValueList className={className} header="Summary" entries={items}/>;
};

const formatTasksSummary = (tasks: Array<TaskInfo>): string => {
    const tasksCnt = tasks.length;
    const projectsCnt = calcNumberOfProjects(tasks);

    return `${formatNumeral(tasksCnt, "task")} executed in ${formatNumeral(projectsCnt, "project")}`;
};

const calcNumberOfProjects = (tasks: Array<TaskInfo>): number => {
    const projectsNames = tasks.map(task => task.path).map(path => path.substring(0, path.lastIndexOf(":")));
    return new Set(projectsNames).size;
};

export default pure(Summary);