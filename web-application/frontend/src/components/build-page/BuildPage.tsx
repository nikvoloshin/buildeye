import React from 'react';
import {safeParseInt} from "../../utils/utils";
import {RouteComponentProps} from "react-router";
import Build from "../build/Build";

const BuildPage: React.FC<RouteComponentProps<{ id: string }>> = ({match}) => {
    const id = safeParseInt(match.params.id);
    return id === null ? null : <Build id={id}/>;
};

export default BuildPage;