import {IndexedBuildInfo} from "../types/BuildInfo";

export function fetchAllBuilds(callback: (array: Array<IndexedBuildInfo>) => void): void {
    doFetch('/api/builds', callback);
}

export function fetchBuild(id: number, callback: (build: IndexedBuildInfo) => void): void {
    doFetch(`/api/build/${id}`, callback);
}

function doFetch<T>(path: string, callback: (data: T) => void): void {
    fetch(path)
        .then(response => {
            if (!response.ok) {
                throw new Error(response.statusText);
            }
            return response.json() as Promise<T>
        })
        .then(data => callback(data))
        .catch(error => console.log(error))
}