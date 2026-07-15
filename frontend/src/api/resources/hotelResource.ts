import { ApiResources } from '../index.js';

type HotelApiResource =
  | 'getList'
  | 'getById'
  | 'save'
  | 'update';

const baseURL = '/auth/hotel';


export const hotelApiResource: ApiResources<HotelApiResource> = {
  getList: { method: 'get', url: '/api/auth/hotel' },
  getById: { method: 'get', url: '/api/auth/hotel/:id' },
  save: { method: 'post', url: '/api/auth/hotel' },
  update: { method: 'put', url: '/api/auth/hotel' },
};