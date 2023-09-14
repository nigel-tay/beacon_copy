import { createReducer, on } from '@ngrx/store';
import { closeDropdown, openDropdown } from './mobilenav.actions';

export const initialState: string = 'close';

export const mobilenavReducer = createReducer (
    initialState,
    on(closeDropdown, (state) => 'close'),
    on(openDropdown, (state) => 'open')
);