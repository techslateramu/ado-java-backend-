data "azurerm_client_config" "current" {}

module "resource_group" {
  source                                      = "github.com/techslateramu/terraform-modules//azure/resourcegroup"
  environment                                 = var.environment
  location                                    = var.location
  main_project                                = var.main_project
  sub_project                                 = var.sub_project
  tags                                        = merge(var.tags, var.specific_tags)
}

module "key_vault" {
  source                                      = "github.com/techslateramu/terraform-modules//azure/keyvault"
  location                                    = var.location
  environment                                 = var.environment
  main_project                                = var.main_project
  sub_project                                 = var.sub_project
  resource_group_name                         = module.resource_group.name
  tenant_id                                   = data.azurerm_client_config.current.tenant_id
  soft_delete_retention_days                  = var.soft_delete_retention_days
  access_policy                               = local.kv_access_policy
  tags                                        = merge(var.tags, var.specific_tags)
}

module "app_service_plan" {
  source                                      = "github.com/techslateramu/terraform-modules//azure/appserviceplan"
  environment                                 = var.environment
  main_project                                = var.main_project
  sub_project                                 = var.sub_project
  location                                    = var.location
  resource_group_name                         = module.resource_group.name
  asp_sku_size                                = var.asp_sku_size
  asp_os_type                                 = var.asp_os_type  
  tags                                        = merge(var.tags, var.specific_tags)
}

module "function_app" {
  source                              = "github.com/techslateramu/terraform-modules//azure/functionapp"
  location                            = var.location
  environment                         = var.environment
  main_project                        = var.main_project
  sub_project                         = var.sub_project
  resource_group_name                 = module.resource_group.name
  app_service_plan_id                 = module.app_service_plan.app_service_id
  # storage_account_name                = module.storage_account.storage_account_name
  # storage_account_primary_access_key  = module.storage_account.storage_primary_access_key
  # os_type                             = var.fapp_os_type
  # funcion_app_version                 = var.ers_function_app_functions_extension_version

  # app_settings                        = local.app_settings
  # site_config                         = local.site_config
  # identity                            = var.identity
  # create_function_app_slot            = var.create_function_app_slot
  tags                                = merge(var.tags, var.specific_tags)

}

module "storage_account" {
  source                      = "github.com/techslateramu/terraform-modules//azure/storageaccount"
  environment                 = var.environment
  main_project                = var.main_project
  sub_project                 = var.sub_project
  tags                        = merge(var.tags, var.specific_tags)
  resource_group_name         = module.resource_group.name
  location                    = var.location
}

module "application_insights" {
  source                                      = "github.com/techslateramu/terraform-modules//azure/appinsights"
  location                                    = var.location
  resource_group_name                         = module.resource_group.name
  environment                                 = var.environment
  main_project                                = var.main_project
  sub_project                                 = var.sub_project

  application_type                            = var.app_in_application_type
  daily_data_cap_in_gb                        = var.app_in_daily_data_cap_in_gb
  daily_data_cap_notifications_disabled       = var.app_in_daily_data_cap_notifications_disabled
  retention_in_days                           = var.app_in_retention_in_days
  disable_ip_masking                          = var.app_in_disable_ip_masking
  workspace_id                              = module.analytics_workspace.id
  tags                                        = merge(var.tags, var.specific_tags)
}

module "analytics_workspace" {
  source                                      = "github.com/techslateramu/terraform-modules//azure/loganalyticsworkspace"
  location                                    = var.location
  resource_group_name                         = module.resource_group.name
  environment                                 = var.environment
  main_project                                = var.main_project
  sub_project                                 = var.sub_project
  tags                                        = var.tags
}


module "key_vault_secret" {
  source                      = "github.com/techslateramu/terraform-modules//azure/keyvaultsecret"
  key_vault_id                = module.key_vault.key_vault_id
  secret_names                = {
                                  "ApplicationInsightsInstrumentationKey"   = module.application_insights.instrumentation_key
                                  "ApplicationInsightsConnectionString"     = module.application_insights.app_insights_connection_string
                                }
}

