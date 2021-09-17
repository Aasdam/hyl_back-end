package com.hyl.itemapi.controller;

import com.hyl.itemapi.exception.CustomBadRequestException;
import com.hyl.itemapi.model.Category;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.Picture;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.validation.MultipartFileListValidation;
import com.hyl.itemapi.service.CategoryService;
import com.hyl.itemapi.service.ItemService;
import com.hyl.itemapi.service.SubItemService;
import com.hyl.itemapi.service.TrackingSheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "item")
public class ItemController {


    //************************************************** Logger
    private Logger logger = LoggerFactory.getLogger(ItemController.class);


    //************************************************** GET
    @GetMapping("/check-category-name")
    public boolean checkAtomicCategoryName(@RequestParam String name, @Autowired HttpServletRequest request) {
        return !CategoryService.checkAtomicName(name, extractIdUserFromHeader(request));
    }

    @GetMapping("/check-item-name")
    public boolean checkAtomicItemName(@RequestParam String name, @Autowired HttpServletRequest request) {
        return !ItemService.checkAtomicName(name, extractIdUserFromHeader(request));
    }

    @GetMapping("/check-sub-ref")
    public boolean checkAtomicSubItemRef(@RequestParam String reference, @Autowired HttpServletRequest request) {
        return !SubItemService.checkAtomicRef(reference, extractIdUserFromHeader(request));
    }

    @GetMapping("/check-category-depth")
    public boolean checkCategoryDepth(@RequestParam String idCategory, @RequestParam String idCategoryToMove, @RequestParam String type) {
        Long idTargetCategory = !idCategory.equals("null") ? Long.parseLong(idCategory) : null;
        Long idCategoryToMove0 = !idCategoryToMove.equals("null") ? Long.parseLong(idCategoryToMove) : null;
      return CategoryService.checkCategoryDepthConstraintForFrontAsyncValidator(idCategoryToMove0, idTargetCategory, type);
    }

    @GetMapping("/check-max-sub")
    public boolean checkMaxSubItemByUser(@Autowired HttpServletRequest request) {
        return !SubItemService.maxSubItemByUserIsValid(extractIdUserFromHeader(request));
    }

    @GetMapping("/get-comment")
    public String getComment(@RequestParam Long idLoan) {
        return TrackingSheetService.getCommentByIdLoan(idLoan);
    }

    @GetMapping("/")
    public Category getMainCategory(@Autowired HttpServletRequest request) {
        return CategoryService.getMainCategory(extractIdUserFromHeader(request));
    }


    //************************************************** POST
    @PostMapping("/add-child-category")
    public Category addChildCategory(@RequestBody HashMap<String, String> hashMap,
                                     @Autowired HttpServletRequest request) {
        String name = hashMap.get("name");
        Long idParent = hashMap.get("idParent") != null ? Long.parseLong(hashMap.get("idParent")) : null;
        if (name == null) throw new CustomBadRequestException("Le paramètre name ne peut pas être null");
        return CategoryService.addChildCategory(name, idParent, extractIdUserFromHeader(request), false);
    }

    @PostMapping("/add-parent-category")
    public Category addParentCategory(@RequestBody HashMap<String, String> hashMap,
                                  @Autowired HttpServletRequest request) {
        String name = hashMap.get("name");
        Long idChild = hashMap.get("idChild") != null ? Long.parseLong(hashMap.get("idChild")) : null;
        if (idChild == null) throw new CustomBadRequestException("Le paramètre idChild ne peut pas être null");
        if (name == null) throw new CustomBadRequestException("Le paramètre name ne peut pas être null");
        return CategoryService.addParentCategory(name, idChild, extractIdUserFromHeader(request), false);
    }

    @PostMapping("/add-item")
    public Item addItem(@RequestParam(value = "files", required = false) List<MultipartFile> files,
                        @RequestParam(value = "data") Object obj,
                        @Autowired HttpServletRequest request) {

        // Validation des images liées au seul et unique subitem obligatoire lors de l'ajout d'un item
        if (files != null && !files.isEmpty() && !files.get(0).isEmpty()) {
            MultipartFileListValidation.validMultipartFileList(files, null);
        }

        // Conversion en JSON de l'objet JSON reçu
        JSONObject data;
        try {
            data = new JSONObject(obj.toString());
        } catch (JSONException ignored) {
            throw new CustomBadRequestException("Format JSON incorrect");
        }

        // Appel du service item pour la validation des données puis l'ajout en bdd
        return ItemService.addItem(data.optString("name"),
                data.optString("description"),
                data.optLong("idCategory"),
                data.optString("reference"),
                extractIdUserFromHeader(request),
                files);
    }

    @PostMapping("/add-subitem")
    public SubItem addSubItem(@RequestParam(value = "files", required = false) List<MultipartFile> files,
                              @RequestParam(value = "data") Object obj) {

        // Validation des images liées au seul et unique subitem obligatoire lors de l'ajout d'un subitem
        if (files != null && !files.isEmpty() && !files.get(0).isEmpty()) {
            MultipartFileListValidation.validMultipartFileList(files, null);
        }

        // Conversion en JSON de l'objet JSON reçu
        JSONObject data;
        try {
            data = new JSONObject(obj.toString());
        } catch (JSONException ignored) {
            throw new CustomBadRequestException("Format JSON incorrect");
        }

        // Appel du service subitem pour la validation des données puis l'ajout en bdd
        return SubItemService.addSubItem(data.optString("reference"),
                data.optLong("idItem"),
                files);
    }

    @PostMapping("/add-tracking-sheet")
    public SubItem addTrackingSheet(@RequestBody HashMap<String, String> hashMap, @Autowired HttpServletRequest request) {
        String comment = hashMap.get("comment");
        String reference = hashMap.get("reference");
        Long idSubItem = hashMap.get("idSubItem") != null ? Long.parseLong(hashMap.get("idSubItem")) : null;
        Long idLoan = hashMap.get("idLoan") != null ? Long.parseLong(hashMap.get("idLoan")) : null;
        long idUser = extractIdUserFromHeader(request);

        if (idSubItem == null && (reference == null || reference.isBlank()))
            throw new CustomBadRequestException("Les paramètres 'idSubItem' et 'reference' sont null," +
                    " au moins un des deux doit posséder une valeur pour identifier l'objet subitem concerné.");

        TrackingSheetService.addTrackingSheet(comment, idSubItem, reference, idLoan, idUser);

        return (idSubItem != null ? SubItemService.getSubItemById(idSubItem)
                : SubItemService.getSubItemByRef(reference, idUser));
    }


    //************************************************** PATCH
    @PatchMapping("/rename-category")
    public void renameCategory(@RequestBody HashMap<String, String> hashMap) {
        String name = hashMap.get("name");
        Long idCategory = hashMap.get("id") != null ? Long.parseLong(hashMap.get("id")) : null;
        if (idCategory == null) throw new CustomBadRequestException("Le paramètre id ne peut pas être null");
        if (name == null) throw new CustomBadRequestException("Le paramètre name ne peut pas être null");
        CategoryService.renameCategory(idCategory, name);
    }

    @PatchMapping("/move-category")
    public void moveCategory(@RequestBody HashMap<String, String> hashMap) {
        Long idCategory = hashMap.get("id") != null && !hashMap.get("id").equals("null") ? Long.parseLong(hashMap.get("id")) : null;
        Long idCategoryDest = hashMap.get("idParent") != null && !hashMap.get("idParent").equals("null") ? Long.parseLong(hashMap.get("idParent")) : null;
        if (idCategory == null) throw new CustomBadRequestException("Le paramètre id ne peut pas être null");
        CategoryService.moveCategory(idCategory, idCategoryDest, false);
    }

    @PatchMapping("/rename-item")
    public void renameItem(@RequestBody HashMap<String, String> hashMap) {
        String name = hashMap.get("name");
        Long idItem = hashMap.get("id") != null ? Long.parseLong(hashMap.get("id")) : null;
        if (idItem == null) throw new CustomBadRequestException("Le paramètre id ne peut pas être null");
        if (name == null) throw new CustomBadRequestException("Le paramètre name ne peut pas être null");
        ItemService.renameItem(idItem, name);
    }

    @PatchMapping("/move-item")
    public void moveItem(@RequestBody HashMap<String, String> hashMap) {
        Long idItem = hashMap.get("id") != null ? Long.parseLong(hashMap.get("id")) : null;
        Long idCategoryDest = hashMap.get("idCategory") != null ? Long.parseLong(hashMap.get("idCategory")) : null;
        if (idItem == null) throw new CustomBadRequestException("Le paramètre idItem ne peut pas être null");
        ItemService.moveItem(idItem, idCategoryDest);
    }

    @PatchMapping("/rename-subitem")
    public void renameSubItem(@RequestBody HashMap<String, String> hashMap, @Autowired HttpServletRequest request) {
        Long idSubItem = hashMap.get("id") != null ? Long.parseLong(hashMap.get("id")) : null;
        String reference = hashMap.get("reference");
        if (idSubItem == null) throw new CustomBadRequestException("Le paramètre idSubItem ne peut pas être null");
        if (reference == null) throw new CustomBadRequestException("Le paramètre reference ne peut pas être null");
        SubItemService.renameSubItem(idSubItem, reference, extractIdUserFromHeader(request), extractJWTFromHeader(request));
    }

    @PatchMapping("/edit-subitem")
    public SubItem editSubItem(@RequestParam(value = "files", required = false) List<MultipartFile> files,
                                @RequestParam(value = "data") Object obj,
                                @Autowired HttpServletRequest request) {

        // Conversion en JSON de l'objet JSON reçu
        JSONObject data;
        try {
            data = new JSONObject(obj.toString());
        } catch (JSONException ignored) {
            throw new CustomBadRequestException("Format JSON incorrect");
        }

        // Récuprération du subitem en bdd
        SubItem subItem = SubItemService.getSubItemById(data.optLong("idSubItem"));

        // IdUser dans la requête
        long idUser = extractIdUserFromHeader(request);

        // Check manuellement si c'est bien le proprietaire du subitem qui est à l'origine de la requête (bug validateur)
        if (subItem.getItem().getIdUser() != idUser) {
            throw new CustomBadRequestException("L'id renseigné dans le header ne correspond pas à l'id du propriétaire de l'objet.");
        }

        // Extraction des images à supprimer
        List<Long> filesToDel = new ArrayList<>();
        JSONArray filesToDelJSONArray = (data.optJSONArray("filesToDelId"));
        if (filesToDelJSONArray != null) {
            for (int i = 0; i < filesToDelJSONArray.length(); i++) {
                long id = filesToDelJSONArray.optLong(i);
                if (id != 0) filesToDel.add(id);
            }
        }

       // Validation des images
        if (files != null && !files.isEmpty() && !files.get(0).isEmpty()) {
           
            List<Picture> pictures = new ArrayList<>();
            for (Picture picture : subItem.getUrlImages()) {
                if (!filesToDel.contains(picture.getId())) pictures.add(picture);
            }
            MultipartFileListValidation.validMultipartFileList(files, pictures);
        }

        // Appel du service subitem pour la validation des données puis l'ajout en bdd
        return SubItemService.editSubItem(
                data.optString("reference"),
                subItem,
                filesToDel,
                files,
                extractJWTFromHeader(request),
                idUser);
    }

    //************************************************** DELETE
    @DeleteMapping("/delete-category")
    public void deleteCategory(@RequestParam long id) {
        CategoryService.deleteCategory(id);
    }

    @DeleteMapping("/delete-item")
    public void deleteItem(@Autowired HttpServletRequest request, @RequestParam long id) {
        ItemService.deleteItem(id, extractJWTFromHeader(request), extractIdUserFromHeader(request));
    }

    @DeleteMapping("/delete-subitem")
    public void deleteSubItem(@RequestParam long id, @Autowired HttpServletRequest request) {
        SubItem subItem = SubItemService.getSubItemById(id);
        // Check manuellement si c'est bien le proprietaire du subitem qui est à l'origine de la requête (bug validateur)
        long idUser = extractIdUserFromHeader(request);
        if (subItem.getItem().getIdUser() != idUser) {
            throw new CustomBadRequestException("L'id renseigné dans le header ne correspond pas à l'id du propriétaire de l'objet.");
        }
        SubItemService.deleteSubItem(subItem, extractJWTFromHeader(request), idUser);
    }

    @DeleteMapping("/delete-tracking-sheets")
    public SubItem deleteTrackingSheets(@RequestParam List<String> ids, @Autowired HttpServletRequest request) {
        return TrackingSheetService.deleteTrackingSheetsByIds(ids, extractIdUserFromHeader(request));
    }

    @DeleteMapping("/delete-tracking-sheets-by-id-subitem")
    public SubItem deleteTrackingSheetsByIdsSubItem(@RequestParam long idSubItem, @Autowired HttpServletRequest request) {
        return TrackingSheetService.deleteTrackingSheetsByIdSubItem(idSubItem, extractIdUserFromHeader(request));
    }

    @DeleteMapping("/delete-tracking-sheets-by-ids-loan")
    public void deleteTrackingSheetsByIdsLoan(@RequestParam List<String> ids, @Autowired HttpServletRequest request) {
        TrackingSheetService.deleteTrackingSheetsByIdsLoan(ids, extractIdUserFromHeader(request));
    }


    //************************************************** SHARE
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handle (NumberFormatException e) {
        return ResponseEntity.badRequest().body(e);
    }

    public static long extractIdUserFromHeader (HttpServletRequest request) {
        String idUserStr = request.getHeader("idUser");
        if ( idUserStr == null ) {
            throw new CustomBadRequestException("Aucun utilisateur n'est spécifié dans le header 'idUser' de la requête.");
        }
        return Long.parseLong(idUserStr);
    }

    public static String extractJWTFromHeader (HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || token.isBlank()) {
            throw new CustomBadRequestException("Aucun token n'est spécifié dans le header 'AUTHORIZATION' de la requête.");
        }
        return token;
    }
}
