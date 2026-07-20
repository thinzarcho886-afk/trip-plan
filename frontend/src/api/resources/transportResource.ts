import { ApiResources } from '..';

// define api resource name
type TransportApiResource =
  | 'getTransports'
  | 'getTransportById';

const baseUrl = '/auth/transports';

export const transportApiResource: ApiResources<TransportApiResource> = {
  getTransports: {method: 'get', url: baseUrl},
  getTransportById: {method: 'get', url: baseUrl+ '/bus-type/:busTypeId/bus/:busId'}
 
};
