import React from 'react';
import {dateToDatetime, formatDate} from "../../../utils/utils";
import {StyleableComponentProps} from "../../../types/StyleableComponentProps";
import {pure} from "recompose";

interface Props extends StyleableComponentProps {
    date: Date
}

const Date: React.FC<Props> = ({className, date}) => (
    <time className={className} dateTime={dateToDatetime(date)}>
        {formatDate(date)}
    </time>
);

export default pure(Date);