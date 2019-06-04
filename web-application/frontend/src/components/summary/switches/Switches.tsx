import React from 'react';
import {pure} from "recompose";
import {StyleableComponentProps} from "../../../types/StyleableComponentProps";
import {SwitchesInfo} from "../../../types/BuildInfo";
import KeyValueList from "../../key-value-list/KeyValueList";

interface Props extends StyleableComponentProps {
    switchesInfo: SwitchesInfo
}

const Switches: React.FC<Props> = ({className, switchesInfo}) => {
    const items = [
        { name: "Build cache", value: switchesInfo.isBuildCacheEnabled },
        { name: "Configure on demand", value: switchesInfo.isConfigureOnDemand },
        { name: "Continue on failure", value: switchesInfo.isContinueOnFailure },
        { name: "Continuous", value: switchesInfo.isContinuous },
        { name: "Dry run", value: switchesInfo.isDryRun },
        { name: "Offline", value: switchesInfo.isOffline },
        { name: "Parallel", value: switchesInfo.isParallelProjectExecutionEnabled },
        { name: "Re-run tasks", value: switchesInfo.isRerunTasks },
        { name: "Refresh dependencies", value: switchesInfo.isRefreshDependencies }
    ].map(({ name, value }) => ({name, value: value ? "On" : "Off"}));

    return <KeyValueList className={className} header="Switches" entries={items}/>;
};

export default pure(Switches);