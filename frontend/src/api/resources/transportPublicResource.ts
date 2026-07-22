import { ApiResources } from '..';

// Define API Resource names
type TransportApiResource =
  | 'getTransports'
  | 'getTransportById';

// Backend Controller mapping: @RequestMapping("/api/transports")
const baseUrl = '/transports';

export const transportPublicApiResource: ApiResources<TransportApiResource> = {
  getTransports: { 
    method: 'get', 
    url: baseUrl 
  },
  getTransportById: { 
    method: 'get', 
    url: `${baseUrl}/bus-type/:busTypeId/bus/:busId` 
  }
};