import React from 'react';
import {ReactComponent as SuccessIcon} from './success-icon.svg';
import {ReactComponent as FailureIcon} from './failure-icon.svg';
import {StyleableComponentProps} from "../../../types/StyleableComponentProps";
import {pure} from "recompose";

interface Props extends StyleableComponentProps {
    success: boolean
}

const SuccessFlag: React.FC<Props> = ({className, success}) => (
    success ?
        <SuccessIcon className={className}/> :
        <FailureIcon className={className}/>
);

export default pure(SuccessFlag);