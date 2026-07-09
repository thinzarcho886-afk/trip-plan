import { i18n } from '../plugins/i18n';

/**
 * required(v);
 * catch - number, string, boolean inputs
 */
export const required = (v: any) =>
  (typeof v == 'number' && !Number.isNaN(v)) ||
  (typeof v == 'string' && !!v) ||
  (typeof v != 'string' &&
    typeof v != 'number' &&
    (v != null || v != undefined)) ||
  i18n.global.t('required');

export const minLength = (length: number) => (v: string) =>
  !v || v.length >= length || i18n.global.t('minLength', { length: length });

export const maxLength = (length: number) => (v: string) =>
  !v || v.length <= length || i18n.global.t('maxLength', { length: length });

export const minNum = (length: number) => (v: number) =>
  !v || v >= length || i18n.global.t('minNum', { length: length });

export const maxNum = (length: number) => (v: number) =>
  !v || v <= length || i18n.global.t('maxNum', { length: length });

export const email = (v: string) =>
  !v ||
  /^[\w-\.]+@([\w-]+\.)+[\w-]{3,}$/g.test(v) ||
  i18n.global.t('invalid email');

export const password = (v: string) =>
  !v ||
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/.test(v) ||
  i18n.global.t('Password must contain uppercase, lowercase, number and special character');

export const phone = (v: string) =>
  !v ||
  /^(09|\+959)\d{5,9}$/.test(v) ||
  i18n.global.t('invalid phone');

export const fileSize = (size: number, multi: boolean = false) =>
  !multi
    ? (v: any) =>
        !v ||
        !v.length ||
        v[0].size < size * 1000000 ||
        i18n.global.t('fileSize', { size: size })
    : (v: any) =>
        !v ||
        !v.length ||
        v.every((i: any) => i.size < size * 1000000) ||
        i18n.global.t('filesSize', { size: size });

export const minDate = (length: string) => (v: string) =>
  !v || v >= length || i18n.global.t('Invalid Date');

export const maxDate = (length: string) => (v: string) =>
  !v || v <= length || i18n.global.t('Invalid Date');

export const decimalPlaces = (decimalCount: number) => (v: string) => {
  const regexPattern =
    decimalCount === 0
      ? /^-?\d+$/
      : new RegExp(`^-?\\d+(\\.\\d{1,${decimalCount}}){0,1}$`);

  return !v || regexPattern.test(v) || i18n.global.t('invalid number');
};

