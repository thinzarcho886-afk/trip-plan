import { ApiResources } from '../index.js';

type HotelApiResource =
  | 'getList'
  | 'getById'
  | 'save'
  | 'update'
  | 'getDestinationByStatus';

const baseURL = '/auth/hotel';


export const hotelApiResource: ApiResources<HotelApiResource> = {
  getList: { method: 'get', url: '/auth/hotel' },
  getById: { method: 'get', url: '/auth/hotel/:id' },
  save: { method: 'post', url: '/auth/hotel' },
  update: { method: 'put', url: '/auth/hotel' },
  getDestinationByStatus: {
    method: 'put',
    url: '/auth/destination/by-status/:status',
  },
};