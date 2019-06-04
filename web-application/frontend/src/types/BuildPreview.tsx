import {BuildAction} from "./BuildInfo";

export interface BuildPreview {
    id: number
    success: boolean
    project: string
    action: BuildAction
    duration: number
    date: Date
}