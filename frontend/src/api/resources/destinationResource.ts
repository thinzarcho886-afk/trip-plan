import { ApiResources } from '..';

// define api resource name
type DestinationApiResource =
  | 'getDestinations'
  | 'register'
  | 'update'
  | 'getById'
  | 'getByStatus'

const baseUrl = '/auth/destination';

export const destinationApiResource: ApiResources<DestinationApiResource> = {
  getDestinations: { method: 'get', url: baseUrl },
  register: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  getByStatus: { method: 'get', url: baseUrl + '/by-status/:status' },
};

