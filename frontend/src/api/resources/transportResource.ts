import { ApiResources } from '..';

// define api resource name
type TransportApiResource =
  | 'getTransports';

const baseUrl = '/auth/transports';

export const transportApiResource: ApiResources<TransportApiResource> = {
  getTransports: {method: 'get', url: baseUrl},
 
};
