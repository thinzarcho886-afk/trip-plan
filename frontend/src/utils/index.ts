import { format as formatDateAs, parseISO, parse } from 'date-fns';
import { FileMimeType } from '../constants/FileMimiType';
import { AxiosResponse } from 'axios';

declare global {
  interface Navigator {
    msSaveBlob?: (blob: any, defaultName?: string) => boolean;
    msSaveOrOpenBlob?: (blob: any, defaultName?: string) => boolean;
  }
}
interface DownloadFileOptions {
  type: FileMimeType;
}

export const formatDate = (
  date: Date | number,
  pattern: string = 'yyyy-MM-dd'
) => formatDateAs(date, pattern);
// pattern: https://date-fns.org/v2.29.3/docs/format

export const formatDateISO = (date: string, pattern: string = 'yyyy-MM-dd') => {
  const dateObj = parseISO(date);
  return formatDateAs(dateObj, pattern);
};

export const formatTime = (time: string, pattern: string = 'hh:mm a') => {
  const parsedTime = parse(time, 'HH:mm:ss', new Date());
  return formatDateAs(parsedTime, pattern);
};

export const convertImageUrlToBase64 = (imgUrl: string, callback: Function) => {
  const image = new Image();
  image.crossOrigin = 'anonymous';
  image.onload = () => {
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');
    if (ctx) {
      canvas.height = image.naturalHeight;
      canvas.width = image.naturalWidth;
      ctx.drawImage(image, 0, 0);
      const dataUrl = canvas.toDataURL();
      callback && callback(dataUrl);
    }
  };
  image.src = imgUrl;
};

export const convertImageFileToBase64 = (file: any, callback: Function) => {
  const fileReader = new FileReader();
  fileReader.onload = () => {
    const srcData = fileReader.result;
    callback && callback(srcData);
  };
  fileReader.readAsDataURL(file);
};

export const downloadFile = async (
  response: AxiosResponse,
  options: DownloadFileOptions
) => {
  try {
    const { data, headers } = response;
    const { type } = options;

    const blob = new Blob([data], { type });

    // IE doesn't allow using a blob object directly as link href
    // instead it is necessary to use msSaveOrOpenBlob
    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
      window.navigator.msSaveOrOpenBlob(blob);
      return;
    }

    const link = document.createElement('a');
    document.body.appendChild(link);

    let fileName = 'File-' + new Date().getTime();
    const disposition = headers['content-disposition'];
    const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
    const searchedName = fileNameRegex.exec(disposition);

    if (searchedName?.length) {
      fileName = searchedName[1].replace(/['"]/g, '');
    }

    // Create a link pointing to the ObjectURL containing the blob.
    const dataUrl = window.URL.createObjectURL(blob);

    link.href = dataUrl;
    link.download = fileName;
    link.click();

    setTimeout(function () {
      // delay revoking the ObjectURL
      window.URL.revokeObjectURL(dataUrl);
    }, 500);
  } catch (error) {
    console.error('Error downloading file', error);
  }
};

export const arrayBufferToJson = (arrayBuffer: ArrayBuffer): any => {
  const decoder = new TextDecoder('utf-8');
  const jsonString = decoder.decode(arrayBuffer);
  const jsonObject = JSON.parse(jsonString);
  return jsonObject;
};

export const fixedDecimal = (num: number, decimalPlaces: number = 2) => {
  if (num) {
    if (!decimalPlaces || decimalPlaces < 0) decimalPlaces = 0;
    const rounded = Math.pow(10, decimalPlaces);
    return Math.round(num * rounded) / rounded;
  } else return 0;
};

export const fixedDecimalForView = (
  num: number | null,
  decimalPlaces: number = 2
) => {
  if (!num) num = 0;
  return num.toLocaleString('en-US', {
    maximumFractionDigits: decimalPlaces,
    minimumFractionDigits: decimalPlaces,
  });
};

export function printWindow(printContents: string | undefined) {
  if (printContents) {
    const reprintWindow = window.open('', 'MsgWindow', 'width=500,height=500');
    if (reprintWindow) {
      reprintWindow.document.write(printContents);
      setTimeout(() => {
        reprintWindow.print();
        reprintWindow.close();
      }, 600);
    }
  }
}

export const prepareStatusList = <T extends Record<string, string | number>>(
  enumType: T,
  ...filters: Array<T[keyof T]>
) => {
  return Object.fromEntries(
    Object.entries(enumType).filter(([_, value]) =>
      filters.includes(value as T[keyof T])
    )
  );
};

export const sortList = <T>(
  list: T[],
  key: keyof T,
  order: 'asc' | 'desc' = 'asc'
) => {
  return [...list].sort((a, b) => {
    const aValue = a[key];
    const bValue = b[key];

    if (aValue === bValue) return 0;

    if (order === 'asc') {
      return aValue > bValue ? 1 : -1;
    } else {
      return aValue < bValue ? 1 : -1;
    }
  });
};
