package ulaval.glo2003.ui.health.responses;

import ulaval.glo2003.ui.common.responses.ResponseAssembler;

public class GetHealthResponseAssembler implements ResponseAssembler<GetHealthResponseAssemblerArgs,
        GetHealthResponse> {

    @Override
    public GetHealthResponse toResponse(final GetHealthResponseAssemblerArgs args) {
        GetHealthResponse healthResponse = new GetHealthResponse();
        healthResponse.api = args.getApi();
        healthResponse.db = args.getDB();

        return healthResponse;
    }
}
