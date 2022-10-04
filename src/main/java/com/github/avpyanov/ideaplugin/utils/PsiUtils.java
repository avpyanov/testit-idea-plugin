package com.github.avpyanov.ideaplugin.utils;

import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.model.TestStep;
import com.github.avpyanov.ideaplugin.settings.ExportSettingsStorage;
import com.github.avpyanov.ideaplugin.settings.TestRunners;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.avpyanov.ideaplugin.Annotations.*;

public class PsiUtils {

    private static final ExportSettingsStorage exportSettings = ExportSettingsStorage.getInstance();

    public static Map<PsiMethod, TestCase> getTestsFromClass(final PsiElement element) {
        final PsiClass psiClass = (PsiClass) element;
        return Arrays.stream(psiClass.getMethods())
                .filter(m -> m.hasAnnotation(Objects.requireNonNull(exportSettings.getState()).getTestAnnotation()))
                .collect(Collectors.toMap(m -> m, PsiUtils::exportTestCaseFromMethod));
    }

    public static Map<PsiMethod, TestCase> getTests(final PsiElement element) {
        final PsiClass psiClass = (PsiClass) element;
        return Arrays.stream(psiClass.getMethods())
                .filter(m -> m.hasAnnotation(Objects.requireNonNull(exportSettings.getState()).getTestAnnotation()))
                .collect(Collectors.toMap(m -> m, PsiUtils::exportTestCase));
    }

    public static List<TestStep> getSteps(final PsiMethod method) {
        final PsiStatement[] statements = Optional.ofNullable(method.getBody())
                .map(PsiCodeBlock::getStatements)
                .orElse(new PsiStatement[]{});

        List<PsiElement> psiMethods = getPsiMethods(statements);
        List<PsiElement> psiMethodsCallExpr = getPsiMethodsCallExpr(psiMethods);

        List<PsiMethodCallExpression> psiMethodCallExpressionList = psiMethodsCallExpr.stream()
                .filter(PsiMethodCallExpression.class::isInstance)
                .map(PsiMethodCallExpression.class::cast)
                .collect(Collectors.toList());

        return psiMethodCallExpressionList.stream()
                .map(PsiMethodCallExpression::resolveMethod)
                .filter(Objects::nonNull)
                .filter(PsiUtils::isStepMethod)
                .map(m -> m.getAnnotation(ALLURE_STEP_ANNOTATION))
                .map(s -> AnnotationUtil.getStringAttributeValue(s, "value"))
                .map(s -> new TestStep().setName(s))
                .collect(Collectors.toList());
    }

    private static TestCase exportTestCaseFromMethod(final PsiMethod method) {
        final TestCase testCase = new TestCase();
        testCase.setName(getName(method));
        testCase.setEpic(getAnnotationValue(method, ALLURE_EPIC_ANNOTATION));
        testCase.setFeature(getAnnotationValue(method, ALLURE_FEATURE_ANNOTATION));
        testCase.setStory(getAnnotationValue(method, ALLURE_STORY_ANNOTATION));
        testCase.setSteps(getSteps(method));
        return testCase;
    }

    private static TestCase exportTestCase(final PsiMethod method) {
        final TestCase testCase = new TestCase();
        testCase.setName(getName(method));
        testCase.setEpic(getAnnotationValue(method, ALLURE_EPIC_ANNOTATION));
        testCase.setFeature(getAnnotationValue(method, ALLURE_FEATURE_ANNOTATION));
        testCase.setStory(getAnnotationValue(method, ALLURE_STORY_ANNOTATION));
        return testCase;
    }

    public static PsiAnnotation createAnnotation(final String annotation, final PsiElement context) {
        final PsiElementFactory factory = PsiElementFactory.getInstance(context.getProject());
        return factory.createAnnotationFromText(annotation, context);
    }

    public static PsiAnnotation createAutotestAnnotation(final PsiMethod method, final String key) {
        return createAnnotation(String.format("@%s(\"%s\")",
                Objects.requireNonNull(exportSettings.getState()).getAutotestAnnotation(), key), method);
    }

    public static PsiAnnotation createManualTestAnnotation(final PsiMethod method, final String key) {
        return createAnnotation(String.format("@%s(\"%s\")",
                Objects.requireNonNull(exportSettings.getState()).getManualTestAnnotation(), key), method);
    }

    public static void optimizeImports(final PsiJavaFile file) {
        JavaCodeStyleManager.getInstance(file.getProject()).shortenClassReferences(file);
        JavaCodeStyleManager.getInstance(file.getProject()).removeRedundantImports(file);
    }

    public static void addImport(final PsiFile file, final String qualifiedName) {
        if (file instanceof PsiJavaFile) {
            addImport((PsiJavaFile) file, qualifiedName);
        }
    }

    public static void addImport(final PsiJavaFile file, final String qualifiedName) {
        final Project project = file.getProject();
        Optional<PsiClass> possibleClass = Optional.ofNullable(JavaPsiFacade.getInstance(project)
                .findClass(qualifiedName, GlobalSearchScope.everythingScope(project)));
        possibleClass.ifPresent(psiClass -> JavaCodeStyleManager.getInstance(project).addImport(file, psiClass));
    }

    public static String getAutotestId(PsiMethod method) {
        return method.getAnnotation(exportSettings.getState().getAutotestAnnotation())
                .findAttributeValue("value")
                .getText()
                .replace("\"", "");
    }

    public static String getManualTestId(PsiMethod method) {
        return method.getAnnotation(exportSettings.getState().getManualTestAnnotation())
                .findAttributeValue("value")
                .getText()
                .replace("\"", "");
    }

    private static String getName(final PsiMethod method) {
        if (Objects.requireNonNull(exportSettings.getState()).getTestRunner().equals(TestRunners.TESTNG.value())) {
            return getAnnotationAttribute(method, exportSettings.getState().getTestAnnotation(), "description");
        }
        if (Objects.requireNonNull(exportSettings.getState()).getTestRunner().equals(TestRunners.JUNIT5.value())) {
            return getAnnotationValue(method, exportSettings.getState().getTestNameAnnotation());
        }
        return "Test name is not available";
    }

    private static String getAnnotationValue(final PsiMethod method, final String annotation) {
        PsiAnnotation psiAnnotation = method.getAnnotation(annotation);
        if (psiAnnotation != null) {
            return AnnotationUtil.getStringAttributeValue(psiAnnotation, "value");
        } else return method.getName() + " has no annotation " + annotation;
    }

    private static String getAnnotationAttribute(final PsiMethod method, final String annotation, final String attribute) {
        PsiAnnotation psiAnnotation = method.getAnnotation(annotation);
        String value = "";
        if (psiAnnotation != null) {
            value = AnnotationUtil.getStringAttributeValue(psiAnnotation, attribute);
        }
        if (value != null) {
            return value;
        } else return method.getName() + "method nas no attribute " + attribute;
    }

    private static boolean isStepMethod(final PsiMethod method) {
        return method.hasAnnotation(ALLURE_STEP_ANNOTATION);
    }

    private static List<PsiElement> getPsiMethods(final PsiElement[] statements) {
        List<PsiElement> psiElements = new ArrayList<>();
        Predicate<PsiElement> psiDeclaration = PsiDeclarationStatement.class::isInstance;
        Predicate<PsiElement> psiLocalVariable = PsiLocalVariable.class::isInstance;
        Predicate<PsiElement> psiExpression = PsiExpressionStatement.class::isInstance;
        Predicate<PsiElement> psiReference = PsiReferenceExpression.class::isInstance;
        Predicate<PsiElement> psiMethod = PsiMethodCallExpression.class::isInstance;

        List<PsiElement> filteredStatements = Arrays.stream(statements)
                .filter(psiDeclaration.or(psiLocalVariable).or(psiExpression).or(psiReference).or(psiMethod))
                .collect(Collectors.toList());

        for (PsiElement statement : filteredStatements) {
            if (statement.getChildren().length == 0) {
                psiElements.add(statement);
            }
            if (statement.getChildren().length > 0) {
                psiElements.addAll(getPsiMethods(statement.getChildren()));
                psiElements.add(statement);
            }
        }
        return psiElements;
    }

    private static List<PsiElement> getPsiMethodsCallExpr(List<PsiElement> psiElements) {
        List<PsiMethodCallExpression> psiMethodCallExpressionList = psiElements.stream()
                .filter(PsiMethodCallExpression.class::isInstance)
                .map(PsiMethodCallExpression.class::cast)
                .collect(Collectors.toList());

        List<PsiElement> extendedList = new ArrayList<>();
        for (PsiMethodCallExpression callExpression : psiMethodCallExpressionList) {
            extendedList.add(callExpression);
            if (callExpression.resolveMethod().getBody() != null) {
                List<PsiElement> psiMethods = getPsiMethods(callExpression.resolveMethod().getBody().getChildren());
                extendedList.addAll(getPsiMethodsCallExpr(psiMethods));
            }
        }
        return extendedList;
    }

    private PsiUtils() {
    }
}