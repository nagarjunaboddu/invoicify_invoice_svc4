package com.invocify.invoice.controller;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.model.CompanySV;
import com.invocify.invoice.service.CompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invocify/companies")
@AllArgsConstructor
@Tag(name = "Company")
@Validated
public class CompanyController {

	private CompanyService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Company createCompany(@Valid @RequestBody Company company) {
		return service.createCompany(company);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<? extends CompanySV> getAllCompanies(
			@RequestParam(required = false, defaultValue = "false") boolean includeDetail,
			@RequestParam(required = false, defaultValue = "false") boolean includeInactive) {
		return service.getAllCompanies(includeDetail, includeInactive);
	}

	@PatchMapping("/{companyId}/status")
	public Company archiveCompany(@PathVariable UUID companyId) {
		return service.archiveCompany(companyId);
	}

	@Operation(description = "Ability to update company details and also update the active status. All fields are required*",
			summary = "Updates the company detail for given company id")
	@PutMapping("/{companyId}")
	public Company modifyCompany(@PathVariable UUID companyId, @Valid @RequestBody Company company) throws InvalidCompanyException {
		return service.modifyCompany(companyId, company);
	}

}
