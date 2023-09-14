import { createReducer, on } from '@ngrx/store';
import { toggleModal } from './modalopen.actions';

export const initialState: boolean = false;

export const modalOpen = createReducer (
    initialState,
    on(toggleModal, (state) => !state)
);