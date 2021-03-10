import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISms, defaultValue } from 'app/shared/model/sms.model';

export const ACTION_TYPES = {
  FETCH_SMS_LIST: 'sms/FETCH_SMS_LIST',
  FETCH_SMS: 'sms/FETCH_SMS',
  CREATE_SMS: 'sms/CREATE_SMS',
  UPDATE_SMS: 'sms/UPDATE_SMS',
  DELETE_SMS: 'sms/DELETE_SMS',
  RESET: 'sms/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISms>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SmsState = Readonly<typeof initialState>;

// Reducer

export default (state: SmsState = initialState, action): SmsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SMS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SMS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SMS):
    case REQUEST(ACTION_TYPES.UPDATE_SMS):
    case REQUEST(ACTION_TYPES.DELETE_SMS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SMS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SMS):
    case FAILURE(ACTION_TYPES.CREATE_SMS):
    case FAILURE(ACTION_TYPES.UPDATE_SMS):
    case FAILURE(ACTION_TYPES.DELETE_SMS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SMS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SMS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SMS):
    case SUCCESS(ACTION_TYPES.UPDATE_SMS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SMS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/sms';

// Actions

export const getEntities: ICrudGetAllAction<ISms> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SMS_LIST,
    payload: axios.get<ISms>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISms> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SMS,
    payload: axios.get<ISms>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISms> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SMS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISms> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SMS,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISms> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SMS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
