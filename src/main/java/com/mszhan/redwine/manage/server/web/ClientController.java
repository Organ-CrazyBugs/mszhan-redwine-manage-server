package com.mszhan.redwine.manage.server.web;

import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Client;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ClientQuery;
import com.mszhan.redwine.manage.server.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;



    @GetMapping(value = "/search")
    public Object search(ClientQuery query) {
        PaginateResult<Client> result = clientService.queryForPage(query);
        return Responses.newInstance().succeed(result);
    }

    @PostMapping(value = "/add_client")
    public Object addClient(@RequestBody Client client) {
        clientService.saveClient(client);
        return Responses.newInstance().succeed();
    }


    @PutMapping(value = "/update_client")
    public Object updateClient(@RequestBody Client client) {
        clientService.updateClient(client);
        return Responses.newInstance().succeed();
    }

    @DeleteMapping(value = "/delete_by_ids/{ids}")
    public Object deleteById(@PathVariable("ids") String ids) {
        clientService.deleteClient(ids);
        return Responses.newInstance().succeed();
    }




}
