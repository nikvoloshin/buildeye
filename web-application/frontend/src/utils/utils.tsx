export const dateToDatetime = (date: Date): string => (
    `${date.getUTCFullYear()}-${date.getUTCMonth()}-${date.getUTCDate()}`
);

export const formatDate = (date: Date): string => (
    new Intl.DateTimeFormat('en', {
        month: 'short',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric'
    }).format(date)
);

export const formatDuration = (duration: number): string => {
    if (duration < 1000) {
        return `${duration} ms`
    }

    let seconds = duration / 1000;
    if (seconds < 60) {
        return `${seconds.toFixed(1)} sec`;
    }

    let minutes = Math.floor(seconds / 60);
    seconds = Math.floor(seconds - minutes * 60);
    if (minutes < 60) {
        return `${minutes} m ${seconds} sec`
    }

    const hours = Math.floor(minutes / 60);
    minutes = Math.floor(minutes - hours * 60);
    return `${hours} h ${minutes} m`
};

export const safeParseInt = (n: string): number | null => (
    Number.isInteger(+n) ? parseInt(n, 10) : null
);

export const formatNumeral = (n: number, name: string, ending: string = "s"): string => (
    `${n} ${name}${n === 1 ? "" : ending}`
);