import React from 'react';
import {pure} from "recompose";
import {StyleableComponentProps} from "../../../types/StyleableComponentProps";
import {InfrastructureInfo} from "../../../types/BuildInfo";
import KeyValueList from "../../key-value-list/KeyValueList";

interface Props extends StyleableComponentProps {
    infrastructureInfo: InfrastructureInfo
}

const Infrastructure: React.FC<Props> = ({className, infrastructureInfo}) => {
    const items = [
        { name: "Operating system", value: `${infrastructureInfo.osName} ${infrastructureInfo.osVersion}` },
        { name: "CPU cores", value: `${infrastructureInfo.cpuCores}` },
        { name: "Max Gradle workers", value: `${infrastructureInfo.maxGradleWorkers.toString()}` },
        { name: "JRE", value: `${infrastructureInfo.jreName} ${infrastructureInfo.jreVersion}` },
        { name: "JVM", value: `${infrastructureInfo.vmVendor} ${infrastructureInfo.vmName} ${infrastructureInfo.vmVersion}` },
        { name: "Max JVM memory heap size", value: `${infrastructureInfo.maxVMHeapSize} MB` },
        { name: "Locale", value: infrastructureInfo.locale },
        { name: "Default charset", value: infrastructureInfo.defaultCharset }
    ];

    return <KeyValueList className={className} header="Infrastructure" entries={items}/>;
};

export default pure(Infrastructure);