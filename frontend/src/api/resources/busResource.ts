import { ApiResources } from '../index.js';

// define api resource name
type BusApiResource =
  | 'getBuses'
  | 'register'
  | 'update'
  | 'getById';


export const busApiResource: ApiResources<BusApiResource> = {
  getBuses: { method: 'get', url: '/auth/bus'},
  register: { method: 'post', url: '/auth/bus' },
  update: { method: 'put', url: '/auth/bus' },
  getById: { method: 'get', url: '/auth/bus/:id' },
};
