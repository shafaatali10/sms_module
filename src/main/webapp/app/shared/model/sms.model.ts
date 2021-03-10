import { Moment } from 'moment';

export interface ISms {
  id?: number;
  status?: string;
  phoneNumber?: string;
  phoneName?: string;
  dateTime?: string;
  message?: string;
  isPinned?: boolean;
}

export const defaultValue: Readonly<ISms> = {
  isPinned: false,
};
