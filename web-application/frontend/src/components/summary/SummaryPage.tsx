import React from 'react';
import {pure} from "recompose";
import {BuildInfo} from "../../types/BuildInfo";
import Switches from "./switches/Switches";
import Infrastructure from "./infrastructure/Infrastructure";
import Summary from "./summary/Summary";

const SummaryPage: React.FC<{buildInfo: BuildInfo}> = ({buildInfo}) => (
    <>
        <Summary buildInfo={buildInfo}/>
        <Switches switchesInfo={buildInfo.switchesInfo}/>
        <Infrastructure infrastructureInfo={buildInfo.infrastructureInfo}/>
    </>
);

export default pure(SummaryPage);