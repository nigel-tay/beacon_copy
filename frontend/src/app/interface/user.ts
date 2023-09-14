export interface User {
    id: string;
    address: string | undefined;
    email: string;
    username: string;
    password: string;
    lat: number | undefined;
    lng: number | undefined;
    image: string | undefined;
}
