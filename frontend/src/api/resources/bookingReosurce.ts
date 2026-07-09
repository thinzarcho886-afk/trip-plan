import { ApiResources } from '..';

type BookingApiResource = 'getList' | 'getById' | 'save' | 'update';

const baseUrl = '/auth/booking';

export const bookingApiResource: ApiResources<BookingApiResource> = {
  getList: { method: 'get', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
};
