package uk.gov.justice.subscription;

import uk.gov.justice.services.yaml.YamlFileValidator;
import uk.gov.justice.services.yaml.YamlParser;
import uk.gov.justice.subscription.domain.subscriptiondescriptor.SubscriptionsDescriptor;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Parse YAML URLs into {@link SubscriptionsDescriptor}s
 */
public class SubscriptionsDescriptorParser {
    private static final String SCHEMA_LOCATION = "/json/schema/";

    private static final String SUBSCRIPTION_SCHEMA_PATH = SCHEMA_LOCATION + "subscription-schema.json";

    private static final TypeReference<Map<String, SubscriptionsDescriptor>> SUBSCRIPTIONS_DESCRIPTOR_TYPE_REF
            = new TypeReference<Map<String, SubscriptionsDescriptor>>() {
    };

    private static final String SUBSCRIPTIONS_DESCRIPTOR = "subscriptions_descriptor";

    private YamlParser yamlParser;
    private YamlFileValidator yamlFileValidator;
    private SubscriptionHelper subscriptionHelper;


    public SubscriptionsDescriptorParser(final YamlParser yamlParser, final YamlFileValidator yamlFileValidator, final SubscriptionHelper subscriptionHelper) {
        this.yamlParser = yamlParser;
        this.yamlFileValidator = yamlFileValidator;
        this.subscriptionHelper = subscriptionHelper;
    }

    /**
     * Return a Stream of {@link SubscriptionsDescriptor} from a Collection of YAML URLs
     *
     * @param urls the YAML URLs to parse
     * @return Stream of {@link SubscriptionsDescriptor}
     */
    public Stream<SubscriptionsDescriptor> getSubscriptionDescriptorsFrom(final Collection<URL> urls) {
        return urls.stream().map(this::parseSubscriptionDescriptorFromYaml);
    }

    private SubscriptionsDescriptor parseSubscriptionDescriptorFromYaml(final URL url) {
        yamlFileValidator.validate(SUBSCRIPTION_SCHEMA_PATH, url);

        final Map<String, SubscriptionsDescriptor> stringSubscriptionDescriptorMap = yamlParser.parseYamlFrom(url, SUBSCRIPTIONS_DESCRIPTOR_TYPE_REF);
        final SubscriptionsDescriptor subscriptionsDescriptor = stringSubscriptionDescriptorMap.get(SUBSCRIPTIONS_DESCRIPTOR);

        subscriptionHelper.sortSubscriptionsByPrioritisation(subscriptionsDescriptor);
        return subscriptionsDescriptor;
    }
}